package utils.filters;

import models.Account;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Leonti on 2016-03-15.
 */
public class AuthenticationFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Account account = Account.getSessionAccount(request);

        if (account != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            request.getServletContext()
                    .getRequestDispatcher("/account/login.jsp")
                    .forward(request, response);
        }
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
