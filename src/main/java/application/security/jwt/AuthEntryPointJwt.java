package application.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String authExMsg = authException.getMessage();
        logger.error("Unauthorized error: {}", authExMsg);

        String errorMsg;
        int errorStatus;

        if (authExMsg.equals("Bad credentials")) {
            errorMsg = "Incorrect email and password combination";
            errorStatus = HttpServletResponse.SC_ACCEPTED;
        } else {
            errorMsg = authExMsg;
            errorStatus = HttpServletResponse.SC_UNAUTHORIZED;
        }

        response.sendError(errorStatus, errorMsg);

    }
}
