/*
    Author: Luke Collier
    Description: A session class to hold basic data about the current logged in user
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.objects;

/**
 * Created by Joker on 02/05/16.
 */
public class Session {
    private static Long userId = 1L;
    private static User currentUser = new User();

    public static void setSessionId(Long id) {
        userId = id;
    }

    public static Long getSessionId() {
        return userId;
    }
}
