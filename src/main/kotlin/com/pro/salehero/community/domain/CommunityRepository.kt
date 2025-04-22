package com.pro.salehero.community.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository: JpaRepository<Community, Long>, CommunityRepositoryCustom