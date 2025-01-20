package com.kitching.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.kitching.common.COLLECTION_NOTICE
import com.kitching.common.COLLECTION_USER
import com.kitching.domain.datasource.NoticeDataSource
import com.kitching.domain.entities.Notice
import com.kitching.domain.entities.NoticeInfo
import com.kitching.domain.entities.User
import kotlinx.coroutines.tasks.await

class NoticeDataSourceImpl(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) : NoticeDataSource {
    override suspend fun getNoticeInfos(teamId: String): Result<List<NoticeInfo>> {
        return runCatching {
            db.collection(COLLECTION_NOTICE)
                .whereEqualTo("teamId", teamId)
                .get()
                .await()
                .toObjects(Notice::class.java).map {
                    NoticeInfo(
                        notice = it,
                        user = db.collection(COLLECTION_USER).document(it.writerId).get().await().toObject(User::class.java)!!
                    )
                }
        }
    }

    override suspend fun createNotice(
        userId: String,
        teamId: String,
        title: String,
        content: String
    ): Boolean {
        return runCatching {
            db.collection(COLLECTION_NOTICE).add(
                Notice(
                    "",
                    userId,
                    teamId,
                    title
                )
            ).await().apply {
                this.update("id", id).await()
            }
        }.isSuccess
    }

    override suspend fun updateNotice(noticeId: String, title: String, content: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_NOTICE).document(noticeId).update("title", title, "content", content).await()
        }.isSuccess
    }

    override suspend fun deleteNotice(noticeId: String): Boolean {
        return runCatching {
            db.collection(COLLECTION_NOTICE).document(noticeId).delete().await()
        }.isSuccess
    }
}