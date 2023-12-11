//package ro.ds.device_MM.JWT;
//
//
//
//import java.io.IOException;
//
//public class JwtTokenFilter extends OncePerRequestFilter {
//    private static final String SECRET_KEY = "3BkgU1dYop0Ko5tzihFJOlJ5nz2e4Yd82Lpfa7aJ88JsElMxvu7flLtIrdgJryI8";
//
//    @Override
//    protected void doFilterInternal(
//            @NonNull HttpServletRequest request,
//            @NonNull HttpServletResponse response,
//            @NonNull FilterChain filterChain) throws ServletException, IOException {
//        String jwt = extractJwtFromRequest(request);
//
//        if (jwt != null && validateToken(jwt)) {
//            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody();
//            String username = claims.getSubject();
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(username, null, null);
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private String extractJwtFromRequest(HttpServletRequest request) {
//        // Extract JWT from the Authorization header
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    private boolean validateToken(String jwt) {
//        // Implement token validation logic as needed
//        // Example: Check token expiration, issuer, etc.
//        // For simplicity, we are only checking for the presence of a token in this example.
//        return jwt != null && !jwt.isEmpty();
//    }
//}
