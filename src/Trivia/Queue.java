package Trivia;
 
import java.util.NoSuchElementException;
 
/**
 * Generic FIFO queue built on a singly linked list.
 * Front of the queue is head; new items are added at tail.
 *
 * @param <T> the type of item stored
 * @author Colin Smith
 * @version Nov 8, 2026
 */
public class Queue<T>
{
    /**
     * A single link in the chain.
     *
     * @param <T> the type of item stored
     */
    private static class Node<T>
    {
        private final T data;
        private Node<T> next;
 
        Node(T data)
        {
            this.data = data;
            this.next = null;
        }
    }
 
    private Node<T> head;
    private Node<T> tail;
    private int size;
 
    /**
     * Creates an empty queue.
     */
    public Queue()
    {
        head = null;
        tail = null;
        size = 0;
    }
 
    /**
     * Adds an item to the back of the queue.
     *
     * @param item the item to add
     * @throws IllegalArgumentException if item is null
     */
    public void enqueue(T item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("item must not be null.");
        }
        Node<T> node = new Node<>(item);
        if (tail == null)
        {
            head = node;
        }
        else
        {
            tail.next = node;
        }
        tail = node;
        size++;
    }
 
    /**
     * Removes and returns the front of the queue.
     *
     * @return the front item
     * @throws NoSuchElementException if the queue is empty
     */
    public T dequeue()
    {
        if (head == null)
        {
            throw new NoSuchElementException("Queue is empty.");
        }
        T item = head.data;
        head = head.next;
        if (head == null)
        {
            tail = null;
        }
        size--;
        return item;
    }
 
    /**
     * Returns the front of the queue without removing it.
     *
     * @return the front item
     * @throws NoSuchElementException if the queue is empty
     */
    public T peek()
    {
        if (head == null)
        {
            throw new NoSuchElementException("Queue is empty.");
        }
        return head.data;
    }
 
    /**
     * @return true if the queue has no items
     */
    public boolean isEmpty()
    {
        return size == 0;
    }
 
    /**
     * @return the number of items in the queue
     */
    public int size()
    {
        return size;
    }
}