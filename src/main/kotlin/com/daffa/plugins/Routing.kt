package com.daffa.plugins

import com.daffa.routes.*
import com.daffa.service.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()

    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        // User routes
        createUser(userService)
        loginUser(
            userService = userService,
            jwtIssuer = jwtIssuer,
            jwtAudience = jwtAudience,
            jwtSecret = jwtSecret
        )

        // Following routes
        followUser(followService)
        unfollowUser(followService)

        // Post routes
        createPost(
            postService,
            userService
        )
        getPostForFollows(
            postService,
            userService
        )
        deletePost(
            postService,
            userService,
            likeService
        )

        // Like routes
        likeParent(
            likeService,
            userService
        )
        unlikeParent(
            likeService,
            userService
        )

        // Comment routes
        createComment(commentService, userService)
        deleteComment(commentService, userService, likeService)
        getCommentForPost(commentService)
    }
}
