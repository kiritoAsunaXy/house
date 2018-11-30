<!DOCTYPE html>

<html lang="en-US">
 <@common.header/>

<body class="page-sub-page page-agents-listing" id="page-top">
<!-- Wrapper -->
<div class="wrapper">
    <!-- Navigation -->
    <@common.nav/>
    <!-- Page Content -->
    <div id="page-content">
        <!-- Breadcrumb -->
        <div class="container">
            <ol class="breadcrumb">
                <li><a href="#">Home</a></li>
                <li><a href="#">Agents</a></li>
                <li class="active">经纪人列表</li>
            </ol>
        </div>
        <!-- end Breadcrumb -->

        <div class="container">
            <div class="row">
                <!-- Agent Detail -->
                <div class="col-md-9 col-sm-9">
                    <section id="agents-listing">
                        <header><h1>经纪人列表</h1></header>
                        <div class="row">
                          <#list ps.list as agent>
                            <div class="col-md-12 col-lg-6" >
                                <div class="agent">
                                    <a href="/agency/agentDetail?id=${agent.id!}" class="agent-image"><img alt="" src="${agent.avatar}"></a>
                                    <div class="wrapper">
                                        <header><a href="/agency/agentDetail?id=${agent.id!}"><h2>${agent.name!}</h2></a></header>
                                        <dl>
                                            <dt>Phone:</dt>
                                            <dd>${agent.phone!}</dd>
                                            <dt>Email:</dt>
                                            <dd><a href="mailto:#">${agent.email!}</a></dd>
                                            <dt>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
                                            <dd>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dd>
                                            <dt>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dt>
                                            <dd>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</dd>
                                        </dl>
                                    </div>
                                </div><!-- /.agent -->
                            </div><!-- /.col-md-12 -->



                          </#list>
                        </div><!-- /.row -->
                    </section><!-- /#agents-listing -->
                    <!-- Pagination -->
                    <div class="center">
                        <@common.paging ps.pagination/>
                    </div><!-- /.center-->
                </div><!-- /.col-md-9 -->
                <!-- end Agent Detail -->

                <!-- sidebar -->
                <div class="col-md-3 col-sm-3">
                    <section id="sidebar">
                       <@common.search />
                       <#--<@common.hot />-->
                        <aside id="featured-properties">
                            <header><h3>热门房产</h3></header>
    <#list recomHouses as house>
    <div class="property small">
        <a href="/house/detail?id=${house.id}">
            <div class="property-image">
                <img alt="" src="${(house.firstImg)!}" style="width: 100px;height: 75px">
            </div>
        </a>
        <div class="info">
            <a href="/house/detail?id=${house.id}"><h4>${(house.name)!}</h4></a>
            <figure>${(house.address)!} </figure>
            <div class="tag price">￥${(house.price)!} 万</div>
        </div>
    </div><!-- /.property -->
    </#list>
                        </aside><!-- /#featured-properties -->
                    </section><!-- /#sidebar -->
                </div><!-- /.col-md-3 -->
                <!-- end Sidebar -->
            </div><!-- /.row -->
        </div><!-- /.container -->
    </div>
    <!-- end Page Content -->
    <!-- Page Footer -->
    <@common.footer/>
    <!-- end Page Footer -->
</div>

<@common.js/>
<!--[if gt IE 8]>
<script type="text/javascript" src="/assets/js/ie.js"></script>
<![endif]-->
 <script  type="text/javascript" >
    $(document).ready(function() {
          var errorMsg   = "${errorMsg!""}";
          var successMsg = "${successMsg!""}";
          if(errorMsg){ 
              errormsg("error",errorMsg);
          }
          if(successMsg) {
              successmsg("success",successMsg);
          }
        })
        
 </script>
</body>
</html>