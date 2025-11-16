package io.github.pablitohaddad.Carteira.Facil.util;

import io.github.pablitohaddad.Carteira.Facil.model.Role;
import io.github.pablitohaddad.Carteira.Facil.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    private static Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long getAuthenticatedUserId() {
        var auth = getAuth();
        if (auth == null || auth.getPrincipal() == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            return null;
        }
        return ((UserPrincipal) auth.getPrincipal()).getId();
    }

    public static Role getAuthenticatedUserRole() {
        var auth = getAuth();
        if (auth == null || auth.getPrincipal() == null || !(auth.getPrincipal() instanceof UserPrincipal)) {
            return null;
        }
        return ((UserPrincipal) auth.getPrincipal()).getRole();
    }

    public static void checkAdminAuthority() {
        Role r = getAuthenticatedUserRole();
        if (r == null || r != Role.ADMIN) {
            throw new SecurityException("Acesso negado. Requer papel ADMIN.");
        }
    }

    public static void checkOwner(Long resourceOwnerId) {
        Long id = getAuthenticatedUserId();
        if (id == null || !id.equals(resourceOwnerId)) {
            throw new SecurityException("Acesso negado. Você não é o dono deste recurso.");
        }
    }
}