package application.model.response;

import java.util.List;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String id;
    private String email;
    private List<String> roles;

    public JwtResponse(String token, String id, String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.email= email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
