package com.mqped.fims.model.dto;

/**
 * Data Transfer Object (DTO) used to encapsulate a simple message response.
 * <p>
 * The {@code MessageResponseDTO} is typically returned by API endpoints to
 * provide
 * human-readable feedback messages such as operation success, warnings, or
 * errors.
 * </p>
 *
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * @PostMapping("/users/register")
 * public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
 *     userService.register(request);
 *     return ResponseEntity.ok(new MessageResponseDTO("User registered successfully."));
 * }
 * }</pre>
 */
public class MessageResponseDTO {

    /**
     * The response message to be returned by the API.
     * <p>
     * This message typically indicates the result of an operation, such as:
     * <ul>
     * <li>Success confirmations (e.g., "Record created successfully")</li>
     * <li>Error explanations (e.g., "Invalid credentials")</li>
     * <li>Informational notes</li>
     * </ul>
     * </p>
     */
    private String message;

    // --- Constructors ---

    /** Default constructor. */
    public MessageResponseDTO() {
    }

    /**
     * Constructs a {@code MessageResponseDTO} with a specific message.
     *
     * @param message the message describing the operation result
     */
    public MessageResponseDTO(String message) {
        this.message = message;
    }

    // --- Getters and Setters ---

    /** @return the message contained in this response */
    public String getMessage() {
        return message;
    }

    /** @param message sets the response message */
    public void setMessage(String message) {
        this.message = message;
    }
}
