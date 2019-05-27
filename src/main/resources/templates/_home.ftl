<!DOCTYPE html>
<html>
<head>
	<title>Spring Boot and freemarker example</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
<pre>
${value1 }
<#-- ${value2} ${colors} -->


迭代：
<ol>  
		
      <li>List循环：  
            <table border="1">  
            <#list colors as color>
              <tr>  
                <th>${color}</th>  
              </tr>
             </#list>  
            </table>  
        </li> 
        <li>Map循环：  
        	<#list map?keys as key> 
        		<tr> 
        			<td>${key}</td> 
        			<td>${map[key]}</td> 
        		</tr>
        	</#list>

        </li>  
 <span ${user.name} />       
 <span ${user.description} >查找有没有getDescription方法</span>   
 
 定义变量：
    <#assign x = 1> <#-- 创建变量 x -->
    x=${x}
    <#assign x = x+3> <#-- 替换变量 x -->
    x=x+3
    x=${x} 
    <#assign p = "myparam">
    param=${p}
  	
</pre>
</body>
</html>