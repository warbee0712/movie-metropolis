package movie.metropolis.app.screen.settings

import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsFacadeTest : FeatureTest() {

    private lateinit var facade: SettingsFacade

    override fun prepare() {
        facade = FacadeModule().settings(prefs)
    }

    @Test
    fun returns_true() {
        whenever(prefs.filterSeen).thenReturn(true)
        assertTrue(facade.filterSeen)
    }

    @Test
    fun returns_false() {
        whenever(prefs.filterSeen).thenReturn(false)
        assertFalse(facade.filterSeen)
    }

    @Test
    fun returns_false_onError() {
        whenever(prefs.filterSeen).thenThrow(RuntimeException())
        assertFalse(facade.filterSeen)
    }

    @Test
    fun listener_notifies() {
        val latch = CountDownLatch(1)
        facade.addListener {
            latch.countDown()
        }
        facade.filterSeen = false
        latch.await()
    }

    @Test
    fun listener_removes() {
        var value = "success"
        val listener = facade.addListener {
            value = "failure"
        }
        facade.removeListener(listener)
        facade.filterSeen = false
        assertEquals("success", value)
    }

}