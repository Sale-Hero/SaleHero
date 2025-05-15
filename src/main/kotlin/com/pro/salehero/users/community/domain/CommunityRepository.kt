package com.pro.salehero.users.community.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CommunityRepository: JpaRepository<Community, Long>, CommunityRepositoryCustom