package Trivia;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the linked-list Queue.
 */
public class QueueTest
{
    private Queue<String> q;

    @BeforeEach
    void setUp()
    {
        q = new Queue<>();
    }

    @Test
    void newQueueIsEmpty()
    {
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
    }

    @Test
    void enqueueIncreasesSize()
    {
        q.enqueue("a");
        assertFalse(q.isEmpty());
        assertEquals(1, q.size());
        q.enqueue("b");
        assertEquals(2, q.size());
    }

    @Test
    void fifoOrder()
    {
        q.enqueue("a");
        q.enqueue("b");
        q.enqueue("c");
        assertEquals("a", q.dequeue());
        assertEquals("b", q.dequeue());
        assertEquals("c", q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void peekDoesNotRemove()
    {
        q.enqueue("a");
        q.enqueue("b");
        assertEquals("a", q.peek());
        assertEquals("a", q.peek());
        assertEquals(2, q.size());
    }

    @Test
    void peekSeesNewFrontAfterDequeue()
    {
        q.enqueue("a");
        q.enqueue("b");
        q.dequeue();
        assertEquals("b", q.peek());
    }

    @Test
    void dequeueEmptyThrows()
    {
        assertThrows(NoSuchElementException.class, () -> q.dequeue());
    }

    @Test
    void peekEmptyThrows()
    {
        assertThrows(NoSuchElementException.class, () -> q.peek());
    }

    @Test
    void enqueueNullThrows()
    {
        assertThrows(IllegalArgumentException.class, () -> q.enqueue(null));
        assertEquals(0, q.size());
    }

    @Test
    void emptyingThenReusingWorks()
    {
        // Checks that tail is reset when the last node is removed.
        q.enqueue("a");
        q.dequeue();
        assertTrue(q.isEmpty());
        assertThrows(NoSuchElementException.class, () -> q.peek());

        q.enqueue("b");
        q.enqueue("c");
        assertEquals("b", q.peek());
        assertEquals(2, q.size());
        assertEquals("b", q.dequeue());
        assertEquals("c", q.dequeue());
    }

    @Test
    void dequeueToEmptyThenThrows()
    {
        q.enqueue("a");
        q.dequeue();
        assertThrows(NoSuchElementException.class, () -> q.dequeue());
        assertEquals(0, q.size());
    }

    @Test
    void rotateFrontToBack()
    {
        // Same pattern Main uses for wrong answers.
        q.enqueue("a");
        q.enqueue("b");
        q.enqueue(q.dequeue());
        assertEquals("b", q.dequeue());
        assertEquals("a", q.dequeue());
    }

    @Test
    void rotateSingleItem()
    {
        q.enqueue("a");
        q.enqueue(q.dequeue());
        assertEquals(1, q.size());
        assertEquals("a", q.peek());
    }

    @Test
    void interleavedOperations()
    {
        q.enqueue("a");
        q.enqueue("b");
        assertEquals("a", q.dequeue());
        q.enqueue("c");
        assertEquals("b", q.dequeue());
        q.enqueue("d");
        assertEquals("c", q.dequeue());
        assertEquals("d", q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void allowsDuplicates()
    {
        q.enqueue("x");
        q.enqueue("x");
        assertEquals(2, q.size());
    }

    @Test
    void manyItems()
    {
        Queue<Integer> nums = new Queue<>();
        for (int i = 0; i < 10_000; i++)
        {
            nums.enqueue(i);
        }
        assertEquals(10_000, nums.size());
        for (int i = 0; i < 10_000; i++)
        {
            assertEquals(i, nums.dequeue());
        }
        assertTrue(nums.isEmpty());
    }
}
