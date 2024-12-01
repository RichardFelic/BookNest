package com.richardfeliciano.booknest.service;

public interface IJsonMapper {
    <T> T getData(String json, Class<T> clase);
}
