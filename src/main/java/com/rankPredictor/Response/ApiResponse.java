package com.rankPredictor.Response;

import org.springframework.http.HttpStatus;

public class ApiResponse {

  private HttpStatus httpStatus;
  private boolean status;
  private String message;
  private Object result;

  public ApiResponse(
    HttpStatus httpStatus,
    String message,
    Object result,
    boolean status
  ) {
    super();
    this.httpStatus = httpStatus;
    this.message = message;
    this.result = result;
    this.status = status;
  }

  public ApiResponse(Boolean status, String message, Object result) {
    super();
    this.message = message;
    this.result = result;
    this.status = status;
  }

  public ApiResponse(HttpStatus httpStatus, String message) {
    super();
    this.httpStatus = httpStatus;
    this.message = message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public boolean getStatus() {
    return status;
  }
}
