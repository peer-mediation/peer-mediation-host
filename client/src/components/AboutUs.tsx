import "../styles/AboutUs.css"

/**
 * React component to render the about us text. Allows for easy switching between this and the chat
 * section. 
 */
export default function AboutUs() {
  return (
    <div aria-label="about-us" className="about-us">
      {/* adding new words: */}
      <p aria-label="about-us-text" className = "about-us-text"> 
        This is the official website for the Clover High School Peer Mediation club
      </p>
      {/* Clear words button */}
      {/* <button
        onClick={async () => {
          // - query the backend to clear the user's words
       
          // - clear the user's words in the database
          await clearUser();
        }}
      >
        Clear words
      </button> */}

    </div>
  );
}
