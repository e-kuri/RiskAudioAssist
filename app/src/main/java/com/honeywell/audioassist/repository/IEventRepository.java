package com.honeywell.audioassist.repository;

import com.google.firebase.firestore.Query;

public interface IEventRepository {

    Query getEventListQuery();

}
