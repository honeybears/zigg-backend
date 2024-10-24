package soma.achoom.zigg.repository

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import soma.achoom.zigg.TestConfig
import soma.achoom.zigg.board.entity.Board
import soma.achoom.zigg.board.repository.BoardRepository
import soma.achoom.zigg.comment.entity.Comment
import soma.achoom.zigg.comment.repository.CommentRepository
import soma.achoom.zigg.data.DummyDataUtil
import soma.achoom.zigg.post.entity.Post
import soma.achoom.zigg.post.entity.PostLike
import soma.achoom.zigg.post.repository.PostLikeRepository
import soma.achoom.zigg.post.repository.PostRepository
import soma.achoom.zigg.user.repository.UserRepository
import java.lang.Thread.sleep
import kotlin.test.Test

@SpringBootTest(classes = [TestConfig::class])
@Transactional
@ActiveProfiles("test")
class PostRepositoryTest {
    @Autowired
    private lateinit var commentRepository: CommentRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var postLikeRepository: PostLikeRepository

    @Autowired
    private lateinit var boardRepository: BoardRepository

    @Autowired
    private lateinit var dummyDataUtil: DummyDataUtil

    @Autowired
    private lateinit var postRepository: PostRepository

    @BeforeEach
    fun setUp() {
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "test",
        )
        for (i in 1..10) {
            val users = dummyDataUtil.createDummyUserList(i)
            board.posts.add(
                postRepository.save(
                    Post(
                        creator = user,
                        likes = mutableSetOf(),
                        likeCnt = i,
                        title = "title$i",
                        textContent = "content$i",
                        board = board

                    )
                )
            )
        }
        boardRepository.save(board)
    }

    @Test
    fun `hottest post while 3 days`() {
        val posts = postRepository.findBestPosts(Pageable.ofSize(2))
        assert(posts.size == 2)
        assert(posts[0].likeCnt == 10)
        assert(posts[1].likeCnt == 9)
    }

    @Test
    fun `delete post`() {
        val post = postRepository.findAll()[0]
        val user1 = userRepository.save(dummyDataUtil.createDummyUser())

        val postLike1 = PostLike(user = user1, post = post)
//        post.likes.add(postLike1)
//        user1.likedPosts.add(postLike1)
        val user2 = userRepository.save(dummyDataUtil.createDummyUser())

        val postLike2 = PostLike(user = user2, post = post)
//        post.likes.add(postLike2)
//        user2.likedPosts.add(postLike2)
        postLikeRepository.save(postLike2)

        postRepository.delete(post)

        assert(postRepository.findById(post.postId!!).isEmpty)
        assert(user1.likedPosts.isEmpty())
        assert(user2.likedPosts.isEmpty())
        assert(postLikeRepository.findAll().isEmpty())

    }

    @Test
    fun `post like persist test`(){
        val post = postRepository.findAll()[0]
        val user = dummyDataUtil.createDummyUser()

        val postLike = PostLike(user = user, post = post)
//        post.likes.add(postLike)
//        user.likedPosts.add(postLike)
        postLikeRepository.save(postLike)

        assert(postLikeRepository.findAll().size == 1)
        assert(postLikeRepository.findAll()[0].post == post)
        assert(postLikeRepository.findAll()[0].user == user)
        assert(user.likedPosts.first().post == post)
        assert(user.likedPosts.size == 1)

    }

    @Test
    fun `post like delete test`() {
        val post = postRepository.findAll()[0]
        val user = userRepository.save(dummyDataUtil.createDummyUser()) // user를 영속화하여 저장

        // PostLike 생성 및 추가
        val postLike = PostLike(user = user, post = post)
//        post.likes.add(postLike)
//        user.likedPosts.add(postLike)
        postLikeRepository.save(postLike)

        // 검증: 좋아요 추가 후 상태 확인
        assert(postLikeRepository.findAll().size == 1)
        assert(postLikeRepository.findAll()[0].post == post)
        assert(postLikeRepository.findAll()[0].user == user)
        assert(user.likedPosts.first().post == post)

        // 좋아요 삭제
//        post.likes.remove(postLike)
//        user.likedPosts.remove(postLike)
        postLikeRepository.delete(postLike)

        // 검증: 좋아요 삭제 후 상태 확인
        assert(postLikeRepository.findAll().isEmpty()) //failed
        assert(postRepository.findById(post.postId!!).get().likes.isEmpty())
        assert(user.likedPosts.isEmpty()) // 이 부분이 이제 성공해야 합니다.
    }

    @Test
    fun `when save post, it should save board`(){
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "board",
        )
        boardRepository.save(board)

        val post = Post(
            creator = user,
            likes = mutableSetOf(),
            title = "title",
            textContent = "content",
            board = board
        )
        board.posts.add(post)
        postRepository.save(post)

        assert(boardRepository.findById(board.boardId!!).get().posts.size == 1) // failed reason: board.posts.size == 0
    }

    @Test
    fun `when delete post it should delete comments`(){
        val user = dummyDataUtil.createDummyUser()
        val board = Board(
            name = "board",
        )
        boardRepository.save(board)
        val post = Post(
            creator = user,
            likes = mutableSetOf(),
            title = "title",
            textContent = "content",
            board = board
        )
        board.posts.add(post)
        postRepository.save(post)

        val comment1 = Comment(
            creator = user,
            textComment = "comment"
        )
        post.comments.add(comment1)
        val comment2 = Comment(
            creator = user,
            textComment = "comment"
        )
        comment1.replies.add(comment2)
        postRepository.save(post)

        postRepository.delete(post)
        println(commentRepository.findCommentsByCreator(user).size)
        assert(commentRepository.findCommentsByCreator(user).isEmpty())
    }

}