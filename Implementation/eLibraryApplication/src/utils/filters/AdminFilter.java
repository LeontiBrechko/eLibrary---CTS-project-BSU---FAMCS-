package utils.filters;

import models.Account;
import models.enums.AccountRole;
import models.enums.AccountState;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Leonti on 2016-05-18.
 */
public class AdminFilter implements Filter {
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
            if (account.getState() == AccountState.ACTIVE && account.getRole() == AccountRole.ADMIN) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                request.setAttribute("errorMessage", "Your account doesn't have proper privileges");
                request.getServletContext()
                        .getRequestDispatcher("/index.jsp")
                        .forward(request, response);
            }
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
