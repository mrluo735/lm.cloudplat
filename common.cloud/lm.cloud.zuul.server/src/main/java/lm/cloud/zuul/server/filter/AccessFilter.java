/**
 * @title AccessFilter.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.zuul.apigateway.filter
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月13日上午11:08:57
 * @version v1.0.0
 */
package lm.cloud.zuul.server.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/** 
 * @ClassName: AccessFilter 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author mrluo735 
 * @date 2017年3月13日 上午11:08:57 
 *  
 */
public class AccessFilter extends ZuulFilter {
	private final Logger logger = LoggerFactory.getLogger(AccessFilter.class);
	
	@Override
    public String filterType() {
        return "pre";
    }
    @Override
    public int filterOrder() {
        return 0;
    }
    @Override
    public boolean shouldFilter() {
        return true;
    }
    
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("accessToken");
        if(accessToken == null) {
        	logger.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            return null;
        }
        logger.info("access token ok");
        return null;
    }
}
