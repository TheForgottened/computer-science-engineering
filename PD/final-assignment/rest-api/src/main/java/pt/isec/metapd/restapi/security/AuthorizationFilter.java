package pt.isec.metapd.restapi.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;

import static pt.isec.metapd.RestApi.tokenManager;

public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null) {
            String[] authHeaderArr = authHeader.split(" ");

            if (authHeaderArr[0].equalsIgnoreCase("BEARER")) {
                String token = authHeaderArr[1];

                if (token != null && tokenManager.isTokenValid(token)) {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("USER"));

                    UsernamePasswordAuthenticationToken uPAT =
                            new UsernamePasswordAuthenticationToken(token, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(uPAT);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public static String getTokenFromAuth(String auth) throws UnknownFormatConversionException {
        String[] authHeaderArr = auth.split(" ");

        if (authHeaderArr[0].equalsIgnoreCase("BEARER")) {
            return authHeaderArr[1];
        }

        throw new UnknownFormatConversionException("Invalid type of authentication string.");
    }

}
