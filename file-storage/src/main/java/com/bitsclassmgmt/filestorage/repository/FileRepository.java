package com.bitsclassmgmt.filestorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsclassmgmt.filestorage.model.File;

public interface FileRepository extends JpaRepository<File, String> {
}
