package velocity.velocityandangleinput;

import java.util.Scanner;

/* QUESTION 1 (I)
Required :
is the user to input both Initual Velocity(V) and Launch angle θ 
both the input Velocity are to be used in the whole program
Alows User input of both Velocity and Launch angle which is to be used
in our Formulaz
*/

public class VelocityandAngleinput {

        public static void main(String[] args) {
                // Scanner triggered for a user to input Velocity (V)

                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the initial velocity (m/s): ");

                double velocity;
                velocity = scanner.nextDouble();

                // Scanner triggered for a user to input launch angle θ

                System.out.println("Enter the launch angle (degrees): ");
                double angleDegrees;
                angleDegrees = scanner.nextDouble();

                /*
                 * QUESTION 1 (II)(a)
                 * Required is:
                 * TIME OF FLIGHT IS CALCULATED
                 * T=2Vsin(θ)/G
                 * ⋅Where T= Time of Flight
                 * V=Initial Velocity
                 * θ=Laugh angle indegrees
                 * GRAVITY=9.81 Acceleration due to gravity in m/s^2
                 */

                double angleRadians;
                // Convert angle to radians
                angleRadians = Math.toRadians(angleDegrees);
                final double GRAVITY = 9.8;
                // Acceleration due to gravity in m/s^2 is constant thats why we added a ststic
                // identifier final

                double timeOfFlight;
                timeOfFlight = (2 * velocity * Math.sin(angleRadians)) / GRAVITY;

                System.out.printf("Time of Flight: %.2f seconds\n", timeOfFlight);

                /*
                 * QUESTION 1 (II)(b)
                 * Required is
                 * MAXIMUM HEIGHT
                 * H=2(V0​⋅sin(θ))2​/2G
                 * Where H= Maximum Height in meters
                 * V=Initial Velocity
                 * θ=Laugh angle
                 * GRAVITY=9.81
                 */
                double maxHeight = Math.pow(velocity * Math.sin(angleRadians), 2) / (2 * GRAVITY);
                System.out.printf("Maximum Height: %.2f meters\n", maxHeight);

                /*
                 * QUESTION 1 (II)(C)
                 * Required is
                 * Horizotal Range
                 * R=V*V​⋅sin(2θ)​/G
                 * Where H= Horizontal Range in meters
                 * V=Initial Velocity
                 * θ=Laugh angle
                 * GRAVITY=9.81
                 */
                double horizontalRange;
                horizontalRange = Math.pow(velocity, 2) * Math.sin(2 * angleRadians) / GRAVITY;
                System.out.printf("Horizontal Range: %.2f meters\n", horizontalRange);

                System.out.println("\n======= Results =======Java programming is fun5");
                System.out.printf("Time of Flight     : %.2f seconds\n", timeOfFlight);
                System.out.printf("Maximum Height     : %.2f meters\n", maxHeight);
                System.out.printf("Horizontal Range   : %.2f meters\n", horizontalRange);

                // JAVA PROGRAMING IS FUN
        }
}
