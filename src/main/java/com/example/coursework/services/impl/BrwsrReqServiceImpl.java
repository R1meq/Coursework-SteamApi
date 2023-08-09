package com.example.coursework.services.impl;

import com.example.coursework.filestorage.ipml.BrwsrReqFileStorageImpl;
import com.example.coursework.models.BrwsrReq;
import com.example.coursework.services.BrwsrReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class BrwsrReqServiceImpl implements BrwsrReqService {

    private final BrwsrReqFileStorageImpl brwsrReqFileStorageImpl;

    @Autowired
    public BrwsrReqServiceImpl(final BrwsrReqFileStorageImpl brwsrReqFileStorageImpl) {
        this.brwsrReqFileStorageImpl = brwsrReqFileStorageImpl;
    }


    @Override
    public List<BrwsrReq> getAll() {
        return brwsrReqFileStorageImpl.getList();
    }

    @Override
    public BrwsrReq getById(final Integer id) {
        return brwsrReqFileStorageImpl.getHashMap().get(id);
    }

    @Override
    public BrwsrReq create(final BrwsrReq entity) {
        brwsrReqFileStorageImpl.checkNameOfBrwsrReq(entity);
        Integer id = brwsrReqFileStorageImpl.incrementIdCounter();
        entity.setBrwsrReqId(id);
        brwsrReqFileStorageImpl.getHashMap().put(id, entity);
        brwsrReqFileStorageImpl.writeToFile(entity, BrwsrReqFileStorageImpl.FILE_PATH);
        return entity;
    }

    @Override
    public BrwsrReq update(final Integer id, final BrwsrReq entity) {
        brwsrReqFileStorageImpl.checkNameOfBrwsrReq(entity);
        brwsrReqFileStorageImpl.deleteFromHashSet(id);
        entity.setBrwsrReqId(id);
        brwsrReqFileStorageImpl.getHashMap().replace(id, entity);
        brwsrReqFileStorageImpl.updateFromFile(id, entity, BrwsrReqFileStorageImpl.DIRNAME);
        return entity;
    }

    @Override
    public void deleteById(final Integer id) {
        brwsrReqFileStorageImpl.deleteFromHashSet(id);
        brwsrReqFileStorageImpl.getHashMap().remove(id);
        brwsrReqFileStorageImpl.deleteFromFile(id, BrwsrReqFileStorageImpl.DIRNAME);
    }
}
