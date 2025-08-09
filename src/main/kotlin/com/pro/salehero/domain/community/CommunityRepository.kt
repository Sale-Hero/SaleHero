package com.pro.salehero.domain.community

import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository: JpaRepository<Community, Long>, CommunityRepositoryCustom