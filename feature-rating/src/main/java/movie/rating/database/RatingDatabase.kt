package movie.rating.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RatingStored::class
    ],
    version = 1
)
internal abstract class RatingDatabase : RoomDatabase() {

    abstract fun rating(): RatingDao

}