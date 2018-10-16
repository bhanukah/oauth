<%@page language="java"
        contentType="text/html; charset=ISO-8859-1"
        pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
    <title>Google OAuth2</title>
    <script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
    <script>
        $(document).ready(function () {
            $("#btnlogin").click(makeRequest);
        });

        function makeRequest() {
            // Define properties
            var AUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/v2/auth";
            var CLIENT_ID = "13840819056-agafbse9s20gjf7h4urv49q1lbhqh7pr.apps.googleusercontent.com";
            var REDIRECT_URI = "http://localhost:8080/oauth2/callback";
            var SCOPE = "https://www.googleapis.com/auth/drive";
            var RESPONSE_TYPE = "code";

            // Build authorization request endpoint
            // According OAuth 2 specification, all the request parameters should be URL encoded
            var requestEndpoint = AUTH_ENDPOINT + "?" +
                    "response_type=" + encodeURIComponent(RESPONSE_TYPE) + "&" +
                    "client_id=" + encodeURIComponent(CLIENT_ID) + "&" +
                    "redirect_uri=" + encodeURIComponent(REDIRECT_URI) + "&" +
                    "scope=" + encodeURIComponent(SCOPE);

            // Send to authorization request endpoint
            window.location.href = requestEndpoint;
        }
    </script>
</head>
<body>

<h1>Login</h1>

<input type="button" value="Sign in" id="btnlogin" width="150"/>

</body>
</html>