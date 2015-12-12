package org.clayman.waterblocks.service;

import org.clayman.waterblocks.dao.ResultDAO;
import org.clayman.waterblocks.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResultServiceImpl implements ResultService {
    private ResultDAO resultDAO;

    @Autowired
    public void setResultDAO(ResultDAO resultDAO) {
        this.resultDAO = resultDAO;
    }

    @Override
    public long save(Result result) {
        return resultDAO.save(result);
    }

    @Override
    public Result getById(long id) {
        return resultDAO.getById(id);
    }

    @Override
    public List<Result> getList() {
        return resultDAO.getList();
    }
}
