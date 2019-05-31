package com.myquora.myquora.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class SensitiveService implements InitializingBean{
	private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
	
	/**
	 * 默认敏感词替换符
	 */
	private static final String DEFAULT_REPLACEMENT = "敏感词"; 
	
	private class TrieNode{
		private boolean end = false;
		// 可能不全是小写英文字母，建立map
		private Map<Character, TrieNode> next = new HashMap<>();
		
		TrieNode getNext(Character key) {
			return next.get(key);
		}
		
		void addNext(Character key, TrieNode node) {
			next.put(key, node);
		}
		
		int getNextCount() {
			return next.size();
		}
		
		boolean isEnd() {
			return end;
		}
		void setEnd(boolean end) {
			this.end = end;
		}
	}
	
	private void addWord(String word) {
		TrieNode tmpNode = rootNode;
		for(int i = 0; i < word.length(); i++) {
			Character c = word.charAt(i);
			if(isSymbol(c)) {
				continue; //不合法字符直接过滤掉
			}
			if( tmpNode.getNext(c) == null) {
				tmpNode.addNext(c, new TrieNode());
			}
			tmpNode = tmpNode.getNext(c);
			if(i == word.length()-1) {
				tmpNode.setEnd(true);
			}
		}
	}
	
	/**
	 * 根节点
	 */
	private TrieNode rootNode = new TrieNode();
	
	/**
	 * 判断是否  不合法直接过滤掉
	 */
	private boolean isSymbol(char c) {
		int ic = (int) c;
		//东亚文字
		return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic> 0x9FFF);
	}
	
	
	/**
	 * 过滤敏感词方法
	 */
	public String filter(String text) {
		addWord("菜鸡");
		if(StringUtils.isBlank(text)) {
			return text;
		}
		String replacement = DEFAULT_REPLACEMENT;
		StringBuilder sb = new StringBuilder();
		
		TrieNode tmpNode = rootNode;
		int begin = 0;//回滚到的位置
		int position = 0;// 当前比较位置
		
		while(position < text.length()) {
			char c = text.charAt(position);
			// 跳过(过滤）非法字符
			if(isSymbol(c)) {
				if(tmpNode == rootNode) {
					sb.append(c); //开头的话可以直接处理了
					begin++; //不是开头的话仅对position++
				}
				position++;
				continue;
			}
			tmpNode = tmpNode.getNext(c);
			if(tmpNode == null) {// 匹配结束，当前begin没问题
				sb.append(c);
				begin++;
				position = begin;
				tmpNode = rootNode;
			} else if(tmpNode.isEnd()) { // 发现敏感词
				sb.append(replacement);
				position++;
				begin = position;
				tmpNode = rootNode;
			} else { // 继续比较
				position++;
			}
		}
		sb.append(text.substring(begin));
		return sb.toString();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
//		rootNode = new TrieNode();
//		try {
//			// 读取文件中的敏感词
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
//					Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt")));
//			String line;
//			while((line = bufferedReader.readLine()) != null) {
//				addWord(line.trim());
//				System.out.println(line.trim());
//			}
//		} catch (Exception e) {
//			logger.error("读取敏感词文件失败" + e.getMessage());
//		}
        rootNode = new TrieNode();

        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
	}
	
	public static void main(String[] args) {
		// simple test
		System.out.println(new SensitiveService().filter("你好 菜△鸡"));
	}

	
}
