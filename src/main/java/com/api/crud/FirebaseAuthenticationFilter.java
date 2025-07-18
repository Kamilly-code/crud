package com.api.crud;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Order(1)
@Component
@DependsOn("firebaseConfig")
@Lazy(false)
public class FirebaseAuthenticationFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger log = LoggerFactory.getLogger(FirebaseAuthenticationFilter.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        log.info("Interceptando request em {}", path);

        // Ignora rotas públicas
        if (path.startsWith("/public/") || path.equals("/health") || path.equals("/ping") || path.equals("/favicon.ico")) {
            log.info("Rota pública liberada: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

                log.info("Token descifrado con éxito. UID: {}", decodedToken.getUid());

                request.setAttribute("firebaseUserId", decodedToken.getUid());
                request.setAttribute("firebaseUserEmail", decodedToken.getEmail());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(decodedToken.getUid(), null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);

            } catch (FirebaseAuthException e) {
                log.error("Token Firebase inválido: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token de Firebase no válido");
            } catch (Exception e) {
                log.error("Error inesperado en el filtro de Firebase: {}", e.getMessage(), e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno en el filtro");
            }
        } else {
            log.warn("Header de autorización ausente o no válido: {}", authHeader);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Header de autorización ausente o no válido");
        }
    }
}