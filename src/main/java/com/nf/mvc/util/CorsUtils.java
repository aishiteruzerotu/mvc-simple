package com.nf.mvc.util;

import com.nf.mvc.support.HttpMethod;

import javax.servlet.http.HttpServletRequest;

import static com.nf.mvc.support.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static com.nf.mvc.support.HttpHeaders.ORIGIN;

public abstract class CorsUtils {

	/**
	 * 判断是否是跨域请求
	 * Returns {@code true} if the request is a valid CORS one by checking {@code Origin}
	 * header presence and ensuring that origins are different.
	 */
	public static boolean isCorsRequest(HttpServletRequest request) {
		String origin = request.getHeader(ORIGIN);
		if (origin == null) {
			return false;
		}
		return true;
		//TODO:更好的实现是下面这样,但spring中这些url解析的类太多了，复制不过来。。。简化为只要有origin就表示是跨域请求
		/*UriComponents originUrl = UriComponentsBuilder.fromOriginHeader(origin).build();
		String scheme = request.getScheme();
		String host = request.getServerName();
		int port = request.getServerPort();
		return !(ObjectUtils.nullSafeEquals(scheme, originUrl.getScheme()) &&
				ObjectUtils.nullSafeEquals(host, originUrl.getHost()) &&
				getPort(scheme, port) == getPort(originUrl.getScheme(), originUrl.getPort()));*/

	}


	/**
	 * 判断是否是"预检请求"
	 * Returns {@code true} if the request is a valid CORS pre-flight one by checking {code OPTIONS} method with
	 * {@code Origin} and {@code Access-Control-Request-Method} headers presence.
	 */
	public static boolean isPreFlightRequest(HttpServletRequest request) {
		return (HttpMethod.OPTIONS.matches(request.getMethod()) &&
				request.getHeader(ORIGIN) != null &&
				request.getHeader(ACCESS_CONTROL_REQUEST_METHOD) != null);
	}

}