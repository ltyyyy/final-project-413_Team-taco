package request;

public class CustomParser {

  // extract java useable values from a raw http request string
  // https://developer.mozilla.org/en-US/docs/Web/HTTP/Messages
  public static ParsedRequest parse(String request){
    String[] lines = request.split("(\r\n|\r|\n)");
    String requestLine = lines[0];
    String[] requestParts = requestLine.split(" ");
    var result = new ParsedRequest();
    result.setMethod(requestParts[0]);

    var parts = requestParts[1].split("\\?");
    result.setPath(parts[0]);

    if(parts.length == 2){
      System.out.println(parts[1]);
      String[] queryParts = parts[1].split("&");
      for (int i = 0; i < queryParts.length; i++) {
        String[] pair = queryParts[i].split("=");
        result.setQueryParam(pair[0], pair[1]);
      }
    }

    // Todo parse the rest
    // Parse headers
    int i = 1;
    while (i < lines.length && !lines[i].isEmpty()) {
      String[] headerParts = lines[i].split(": ");
      result.setHeaderValue(headerParts[0], headerParts.length > 1 ? headerParts[1] : "");
      parseCookieHeader(headerParts[1], result);
      i++;
    }

    // Parse body (if any)
    if (++i < lines.length) {
      StringBuilder bodyBuilder = new StringBuilder();
      for (; i < lines.length; i++) {
        bodyBuilder.append(lines[i]);
      }
      result.setBody(bodyBuilder.toString());
    }

    return result;
  }
  private static void parseCookieHeader(String cookieHeaderValue, ParsedRequest result) {
    String[] cookies = cookieHeaderValue.split("; ");
    for (String cookie : cookies) {
      String[] cookieParts = cookie.split("=");
      if (cookieParts.length == 2) {
        result.setCookieValue(cookieParts[0], cookieParts[1]);
      }
    }
  }
}
