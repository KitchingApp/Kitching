import android.annotation.SuppressLint
import android.graphics.Color
import com.kitchingapp.domain.entities.Department
import com.kitchingapp.domain.entities.Ingredient
import com.kitchingapp.domain.entities.Notice
import com.kitchingapp.domain.entities.Order
import com.kitchingapp.domain.entities.OrderCategory
import com.kitchingapp.domain.entities.Recipe
import com.kitchingapp.domain.entities.RestaurantTeam
import com.kitchingapp.domain.entities.Schedule
import com.kitchingapp.domain.entities.ScheduleTime
import com.kitchingapp.domain.entities.StaffLevel
import com.kitchingapp.domain.entities.Todo
import com.kitchingapp.domain.entities.TodoCategory
import com.kitchingapp.domain.entities.User
import java.time.LocalDate
import java.time.LocalTime

class RestaurantGenerator {
    companion object {
        @SuppressLint("NewApi")
        fun restaurantList(): List<RestaurantTeam> {
            val owner = User(
                name = "John Doe",
                birth = LocalDate.of(1980, 5, 15),
                email = "johndoe@example.com",
                healthCertificate = LocalDate.of(2024, 1, 1)
            )

            val users = (1..10).map {
                User(
                    name = "User$it",
                    birth = LocalDate.of(1990 + it % 10, it % 12 + 1, it % 28 + 1),
                    email = "user$it@example.com",
                    healthCertificate = LocalDate.of(2024, 1, 1).minusDays(it.toLong())
                )
            }

            val todoCategories = (1..10).map {
                TodoCategory(
                    name = "Category$it",
                    color = Color.rgb(it * 25 % 256, it * 20 % 256, it * 15 % 256)
                )
            }

            val todos = (1..10).map {
                Todo(
                    name = "Task$it",
                    category = todoCategories[it % todoCategories.size]
                )
            }

            val orderCategories = (1..10).map {
                OrderCategory(
                    name = "OrderCategory$it",
                    color = Color.rgb(it * 15 % 256, it * 25 % 256, it * 30 % 256)
                )
            }

            val orders = (1..10).map {
                Order(
                    name = "Order$it",
                    category = orderCategories[it % orderCategories.size],
                    count = it * 2
                )
            }

            val departments = (1..10).map {
                Department(
                    name = "Department$it",
                    color = Color.rgb(it * 20 % 256, it * 30 % 256, it * 25 % 256)
                )
            }

            val staffLevels = (1..10).map {
                StaffLevel(
                    name = "Level$it",
                    department = departments[it % departments.size]
                )
            }

            val scheduleTimes = (1..10).map {
                ScheduleTime(
                    name = "Time$it",
                    startTime = LocalTime.of(it % 24, it * 5 % 60),
                    endTime = LocalTime.of((it + 2) % 24, (it * 7) % 60),
                    color = Color.rgb(it * 10 % 256, it * 15 % 256, it * 20 % 256)
                )
            }

            val schedules = (1..10).map {
                Schedule(
                    applier = users[it % users.size],
                    scheduleTime = scheduleTimes[it % scheduleTimes.size],
                    date = LocalDate.of(2024, (it % 12) + 1, (it % 28) + 1),
                    isFix = it % 2 == 0
                )
            }

            val recipes = (1..10).map {
                Recipe(
                    name = "Recipe$it",
                    ingredients = (1..5).map { idx ->
                        Ingredient(
                            name = "Ingredient$idx",
                            once = idx * 10,
                            twice = idx * 20,
                            each = "Unit$idx"
                        )
                    },
                    steps = (1..5).map { step -> "Step $step for Recipe$it" }
                )
            }

            val notices = (1..10).map {
                Notice(
                    title = "Notice$it",
                    content = "This is the content for Notice$it.",
                    date = LocalDate.of(2024, (it % 12) + 1, (it % 28) + 1),
                    writer = owner
                )
            }

            return listOf(
                RestaurantTeam(
                    teamName = "Team Alpha",
                    owner = owner,
                    managers = users.subList(0, 5),
                    members = users.subList(5, 10),
                    todoCategories = todoCategories,
                    todos = todos,
                    orderCategory = orderCategories,
                    orders = orders,
                    departments = departments,
                    staffLevels = staffLevels,
                    scheduleTimes = scheduleTimes,
                    recipes = recipes,
                    schedules = schedules,
                    notices = notices
                ),
                RestaurantTeam(
                    teamName = "Team Beta",
                    owner = owner,
                    managers = users.subList(0, 5),
                    members = users.subList(5, 10),
                    todoCategories = todoCategories,
                    todos = todos,
                    orderCategory = orderCategories,
                    orders = orders,
                    departments = departments,
                    staffLevels = staffLevels,
                    scheduleTimes = scheduleTimes,
                    recipes = recipes,
                    schedules = schedules,
                    notices = notices
                )
            )
        }
    }
}
