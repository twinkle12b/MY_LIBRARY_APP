package com.react2code.springbootlibrary.dao;

import com.react2code.springbootlibrary.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
