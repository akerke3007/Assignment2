package org.example;

import java.util.List;

public interface IRepository<T> {

    List<T> getAll() throws Exception;
}