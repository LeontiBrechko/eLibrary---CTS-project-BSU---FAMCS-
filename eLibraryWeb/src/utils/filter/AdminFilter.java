package utils.filter;

import entities.user.Role;
import entities.user.User;
import entities.user.UserState;
import utils.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

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
        User user = SessionUtil.getSessionAccount(request);

        if (user != null) {
            if (user.getState() == UserState.ACTIVE &&
                    user.getRoles().stream().filter(userRole -> userRole.getRole() == Role.ADMIN)
                            .collect(Collectors.toList()).size() == 1) {
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
