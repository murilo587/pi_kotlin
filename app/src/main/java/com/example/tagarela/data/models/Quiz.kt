data class GameResponse(
    val id: String,
    val nivel: Int,
    val games: List<Game>
)

data class Game(
    val id: String,
    val correctAnswer: Int,
    val type: Int,
    val quizId: String,
    val items: List<Item>
)

data class Item(
    val id: String,
    val name: String,
    val syllables: String,
    val image: String?,
    val video: String?,
    val audio: String?,
    val category: String,
    val subcategory: String,
    val userId: String?
)

data class ImageCategoryResponse(
    val category: String
)