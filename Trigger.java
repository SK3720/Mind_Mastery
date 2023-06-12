/** 
Class to handle physical walls the player can bump into

    <-------May 29------->
      > separated Obstacle class into separate child classes of Hitbox
      Contributor: Caleb Chue

*/

public class Trigger extends Hitbox {
    int id;

    public Trigger(int xPos, int yPos, int wid, int hgt, int i) {
        super(xPos, yPos, wid, hgt);
        id = i;
    }
    
    public String interactedBehaviour() {
        if (id == 2) return "goto 11";
        if (id == 3) return "paragraph \nOh hi, didn't see you there. *sigh*\nSchool hasn't been too good for me...\nI can't focus in class at all,\nIt's really bringing down my grades.\nBut no one understands what I'm facing though...\nI need a better way to focus in the future...";
        if (id == 4) return "goto 13";
        if (id == 5) return "goto 12";
        if (id == 6) return "goto 14";
        if (id == 7) return "paragraph \nWelcome to our institute, where we explore the ; world of attention disorders. \n Focus in as we explore insights and strategies to ; understand and navigate the challenges they bring.\nAttention disorders, like ADHD, are conditions that ; affect a person's ability to pay attention and control;\n impulsive behavior. These disorders can impact; different areas of life, such as school and;\nrelationships. Actions such as following directions;or completing tasks are a struggle for those who are;\n affected by these disorders, which most heavily; impact children. While these may seem to be;\n simple chores for most people, children affected;by attention disorders are easily distracted and;\n have trouble organizing their thoughts and actions.; The impacts of these disorders include below average;\n  academic performance, difficulty in social;situations, and even low self-esteem as these;\n children are not provided the support they need yet; are still compared with their peers. In order;\nto survive Attention Disorders, various solutions can; be used. Although medicine is available, they often;\n result in severe side effects, and cannot be used; as the main cure for these disorders.;\n Thus, some effective ; and benefical solutions are as follows.\n 1. Creating a Schedule. ;\nCreating a schedule results in many benefits such as; boosted productivity and increased focus.;\n 2. Implementing a Routine.;\n Having a routine is essential to combat the impacts of; attention disorders in daily life. It allows for the;\n person to keep on track, add a systematic structure; to the day and build proper habits, such;\nas exercising or cleaning. Another helpful solution; is also to set time limits to activities.;\n Anyway, that's enough from me.; Visit the blue house to move on!";
        if (id == 8) return "goto 9";
        if (id == 9) return "";
        if (id == 10) return "";
        return "";
    }
    
    public String proximityMessage() {
        // learning sign 1
        if (id == 0) return "Welcome to the Learning Area! \n Head to the Red House to begin.";
        if (id == 1) return "wow";
        if (id == 2) return "Enter the Red House";
        if (id == 3) return "Talk to James";
        if (id == 4) return "Exit the Red House";
        if (id == 5) return "Enter the learning centre";
        if (id == 6) return "Exit the learning centre";
        if (id == 7) return "Talk with Pamela";
        if (id == 8) return "Enter the Blue House";
        if (id == 9) return "Closed";
        if (id == 10) return "To progress, visit the Learning Centre";
        if (id == 11) return "To progress, visit the Blue House";
        if (id == 12) return "Press 'E' to interact with James. \n Learn about the impacts of Attention Disorders.";
        if (id == 13) return "Press 'E' to interact with Pamela. \n Learn about Attention Disorders and solutions to surviving them.";
        if (id == 14) return "Navigate the maze to finish the \n tasks indicated with yellow arrows.";
        
        return "";
    }
}