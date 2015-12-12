package org.clayman.waterblocks.dao;

import org.clayman.waterblocks.domain.Result;

import java.util.List;

public interface ResultDAO {
    long save(Result result);

    Result getById(long id);

    List<Result> getList();
}
