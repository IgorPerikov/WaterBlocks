package org.clayman.waterblocks.service;

import org.clayman.waterblocks.domain.Result;

import java.util.List;

public interface ResultService {
    long save(Result result);

    Result getById(long id);

    List<Result> getList();
}
