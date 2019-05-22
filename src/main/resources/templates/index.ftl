<!DOCTYPE html>
<!-- saved from url=(0029)#signin -->
<html lang="zh-CN" dropeffect="none" class="js is-AppPromotionBarVisible cssanimations csstransforms csstransitions flexbox no-touchevents no-mobile" style="">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" async="" src="../scripts/za-0.1.1.min.js"></script>
    <script async="" src="../scripts/ga.js"></script>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-ZA-Response-Id" content="46acde5c53db46f2806ccad726de9826">
    <title>首页 - 牛客</title>
    <meta name="apple-itunes-app" content="app-id=432274380">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta http-equiv="mobile-agent" content="format=html5;url=https://nowcoder.com/">
    <meta id="znonce" name="znonce" content="d3edc464cf014708819feffde7ddd01e">
    <link rel="search" type="application/opensearchdescription+xml" href="https://nowcoder.com/static/search.xml" title="牛客">
    <link rel="stylesheet" href="../styles/index.css">
    <style>
    .zm-item-answer-author-info a.collapse {margin-top: 0}
    </style>

</head>
<body class="zhi ">

    <div class="zg-wrap zu-main clearfix " role="main">
        <div class="zu-main-content">
            <div class="zu-main-content-inner">
                <div class="zg-section" id="zh-home-list-title">
                    <i class="zg-icon zg-icon-feedlist"></i>最新动态
                    <input type="hidden" id="is-topstory">
                    <span class="zg-right zm-noti-cleaner-setting" style="list-style:none">
                        <a href="https://nowcoder.com/settings/filter" class="zg-link-gray-normal">
                            <i class="zg-icon zg-icon-settings"></i>设置</a></span>
                </div>
                <div class="zu-main-feed-con navigable" data-feedtype="topstory" id="zh-question-list" data-widget="navigable" data-navigable-options="{&quot;items&quot;:&quot;&gt; .zh-general-list .feed-content&quot;,&quot;offsetTop&quot;:-82}">
                    <a href="javascript:;" class="zu-main-feed-fresh-button" id="zh-main-feed-fresh-button" style="display:none"></a>
                    
                    
                    
                    <div id="js-home-feed-list" class="zh-general-list topstory clearfix" data-init="{&quot;params&quot;: {}, &quot;nodename&quot;: &quot;TopStory2FeedList&quot;}" data-delayed="true" data-za-module="TopStoryFeedList">
                        
                    <#list vos as vo>
							
                    <div class="feed-item folding feed-item-hook feed-item-2
                        " feed-item-a="" data-type="a" id="feed-2" data-za-module="FeedItem" data-za-index="">
                            <meta itemprop="ZReactor" data-id="389034" data-meta="{&quot;source_type&quot;: &quot;promotion_answer&quot;, &quot;voteups&quot;: 4168, &quot;comments&quot;: 69, &quot;source&quot;: []}">
                            <div class="feed-item-inner">
                                <div class="avatar">
                                    <a title="${vo.user.name}" data-tip="p$t$amuro1230" class="zm-item-link-avatar" target="_blank" href="https://nowcoder.com/people/amuro1230">
                                        <img src="${vo.user.headUrl}" class="zm-item-img-avatar"></a>
                                </div>
                                <div class="feed-main">
                                    <div class="feed-source" data-za-module="FeedSource">热门回答，来自
                                        <a href="https://nowcoder.com/topic/19562033" data-tip="t$t$19562033" data-token="19562033" data-topicid="3946" target="_blank">人际交往</a>
                                        <a data-follow="t:link" href="javascript:;" class="zg-follow zu-autohide follow-topic zu-edit-button" data-id="3946">关注话题</a></div>
                                    <div class="feed-content" data-za-module="AnswerItem">
                                        <meta itemprop="answer-id" content="389034">
                                        <meta itemprop="answer-url-token" content="13174385">
                                        <h2 class="feed-title">
                                            <a class="question_link" target="_blank" href="/question/${vo.question.id}">${vo.question.title}</a></h2>
                                        <div class="feed-question-detail-item">
                                            <div class="question-description-plain zm-editable-content"></div>
                                        </div>
                                        <div class="expandable entry-body">
                                            <!-- <link itemprop="url" href="/question/19857995/answer/13174385">
                                            <meta itemprop="answer-id" content="389034">
                                            <meta itemprop="answer-url-token" content="13174385"> -->
                                            <div class="zm-item-vote">
                                                <a class="zm-item-vote-count js-expand js-vote-count" href="javascript:;" data-bind-votecount="">4168</a></div>
                                            <div class="zm-votebar" data-za-module="VoteBar">
                                                <button class="up" aria-pressed="false" title="赞同">
                                                    <i class="icon vote-arrow"></i>
                                                    <span class="count">4168</span>
                                                    <span class="label sr-only">赞同</span></button>
                                                <button class="down" aria-pressed="false" title="反对，不会显示你的姓名">
                                                    <i class="icon vote-arrow"></i>
                                                    <span class="label sr-only">反对，不会显示你的姓名</span></button>
                                            </div>
                                            <div class="zm-item-answer-author-info">
                                                <a class="author-link" data-tip="p$b$amuro1230" target="_blank" href="/user/${vo.user.id}">${vo.user.name}</a>
                                             	 <span>${vo.question.createdDate?string('yyyy-MM-dd HH:mm:ss')}</span>
                                            </div>
                                            <div class="zm-item-vote-info" data-votecount="4168" data-za-module="VoteInfo">
                                                <span class="voters text">
                                                    <a href="#" class="more text">
                                                        <span class="js-voteCount">4168</span>&nbsp;人赞同</a></span>
                                            </div>
                                            <div class="zm-item-rich-text expandable js-collapse-body" data-resourceid="123114" data-action="/answer/content" data-author-name="${vo.user.name}" data-entry-url="/question/19857995/answer/13174385">
                                  				<div class="zh-summary summary clearfix">${vo.question.content}</div>
                                             </div>
                                        </div>
                                        <div class="feed-meta">
                                            <div class="zm-item-meta answer-actions clearfix js-contentActions">
                                                <div class="zm-meta-panel">
                                                    <a data-follow="q:link" class="follow-link zg-follow meta-item" href="javascript:;" id="sfb-123114">
                                                        <i class="z-icon-follow"></i>关注问题</a>
                                                    <a href="#" name="addcomment" class="meta-item toggle-comment js-toggleCommentBox">
                                                        <i class="z-icon-comment"></i>${vo.question.commentCount}条评论</a>
                                                    <a href="#" class="meta-item js-thank" data-thanked="false">
                                                        <i class="z-icon-thank"></i>感谢</a>


                                                    <button class="meta-item item-collapse js-collapse">
                                                        <i class="z-icon-fold"></i>收起</button>
                                                </div>
                                            </div>
                                            <a href="#" class="ignore zu-autohide" name="dislike" data-tip="s$b$不感兴趣"></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="undo-dislike-options" data-item_id="2">此内容将不会在动态中再次显示
                                <span class="zg-bull">•</span>
                                <a href="#" class="meta-item revert">撤销</a>
                                <a href="#" class="ignore zu-autohide close"></a>
                            </div>
                        </div>
                    </#list>
                    
                    </div>
                    <a href="javascript:;" id="zh-load-more" data-method="next" class="zg-btn-white zg-r3px zu-button-more" style="">更多</a></div>
            </div>
        </div>
    </div>
   
</body></html>