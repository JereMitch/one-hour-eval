Run instructions:

- Frontend: cd ./frontend -> npm run dev
- Backend: cd ./backend -> ./mvnw.cmd spring-boot:run

This is the notes file that I will tell you about my AI usage and how I completed it. I broke it out into three separate steps:

Side Note: I am also using an AI tool called Wispr Flow to be able to speak directly into my computer so that I don't have to type everything. This is exactly how I would describe this if I were to tell you in person.

1.  The first step that I did is I went to Gemini and I grabbed the README file out of the GitHub repository that you guys provided, and I went into the problem statement and requirements for the test and input that directly into Gemini and had it write me a prompt optimized for Claude code. Since I already had a Claude subscription, I did not feel the need to reach out for an API key or anything like that.

Here is a copy of the prompt I wrote into Gemini:

 I'm creating a very simple e-commerce search filtering site. I will paste in the requirements below: 

You are building a simple product search + AI recommendation widget.
Core flow
User enters a search query (e.g., “running shoes”, “hoodie”, “water bottle”)
Your app fetches products from the Shopify feed (or caches them locally)
Your app filters/searches products (basic substring match is fine)
Your app sends the top 3 matching products to an LLM prompt
Your app displays the AI recommendation explaining:
Which product is best for a beginner
Which product is best for an expert
Why
You may implement the LLM call as either:
A real API call (OpenAI/Anthropic/etc.), or
A mock function that returns a hardcoded “LLM-like” response
🛠 Requirements Must-have (choose a reasonable subset)
A basic UI or CLI/script (frontend preferred, but not required)
Shopify product fetching (live or cached)
Search/filtering logic
LLM prompt that consumes multiple products
Display/output of recommendation
You do not need to
Handle pagination perfectly
Make it production-ready
Over-optimize search
Add authentication
Perfect styling

---

I want you to create a test plan that can be optimized and used in Claude Code so that it can be understood easily. I want the following technologies:
React TypeScript with no Tailwind
I want Vanilla CSS
I want the simple backend to be in Java
I want it to use Hibernate and Spring Boot
I want the testing library to be in Jest
I want the React app to be created using Vite

--END OF PROMPT--

As you can see, I provided the same requirements that you gave me in the GitHub repo, just so they had the same context that I had. I then told exactly what technologies I wanted it to use.

2.  Step two is I took the response from that prompt above and I read through it to make sure that it was following the things that I wanted it to do, and then input it into Claude Code under the planning tool. I specifically asked Claude to ask me clarifying questions and to question me on the structure and architecture of what I am building and how I want it to be structured.

I did not copy any of the questions that Claude Code asked as clarifiers. The session is too long now, and I do not have access to them anymore, so I cannot go find them. However, to summarize these questions, it was just clarifications on what I wanted to use on different tools, like if I wanted to use an H2 database, which I decided to go with because it's a prototype. We just need an in-memory database for it to function on these calls so that it's instantaneous and fast and not wasting time actually connecting to SQL servers and whatnot, while maintaining the similar structure to if we were to use something like Postgres or MySQL.

After the questions got asked and clarified, I gave all the responses I wanted, and it was looking like the structure was how I wanted it to be. I sent it off so that Claude could start building it as a baseline to build off of, so that it can get all the infrastructure and structure sorted out.

While Claude code is building the initial run of this little widget/application, I went to Figma's AI tool and gave it a prompt to write me a search filtering widget/UI to use so that it could be clean and functionally useful. Here is a copy of the prompt I wrote:

--

I want to design a simple Shopify-style search screen with results coming back as cards. I want them to come back as beginner, mid-level, and expert-level cards, with AI-generated responses to explain why each of them is in that category.

--

I then updated it to pull or to create the same mock-up, pulling from KISS.products.json, which is the same URL that I pointed the application to, to give a better idea of roughly what it would look like. Here is a link to that Figma file: https://www.figma.com/make/tm9GHAsH34JwGOELtvQCoo/Shopify-style-search-screen?t=Q4VdDddpATPLTVJx-20&fullscreen=1

I ended up not using this because I ran out of time, sadly. However, Claude and Figma came out with a fairly similar design, so I wasn't too upset by it. Also, the Figma AI misunderstood a little bit and put the AI classification on each of the cards individually rather than giving an overall recommendation, which was required inside of your requirements.

3.  Step three of this process was: after Claude got done building it, I went through each of the files individually to read them to make sure that there were no API keys or any environment variables or anything like that that would be a security risk. I then went into the README files to make sure that there were clear instructions on how to run the app, as well as things to do or quick things to know for testing and whatnot.

After I'd done that, I ran each of the commands locally to make sure they did run and all the framed-out and stubbed-out tests did pass. I then ran it locally inside of my IDE, which is Cursor, and toyed around and played with the UI to make sure that it was doing the things that I wanted it to do.

These steps took longer than expected, which is kind of the point of this exercise being only an hour, but it took longer than expected. I did not have as much time as I would have liked to go into all of the React files to double check the practices on using hooks and state management. Since it is fairly small, it looked clean and it works well, so I wasn't terribly worried about it. However, one thing I did notice is that while searching inside of this little widget, it doesn't handle plurals very well, so it doesn't filter necessarily the greatest. If you type in shoe or sandal or tea, it does return the proper products outside of the JSON object.

That's part of the assessment. You wanted to hear my thought process and working pattern and what I would do next, so here they are. I would go first into the components to make sure that the hooks are being used effectively. Just an effort to make sure that re-renders aren't happening and that we're being efficient with our usage inside the browser. Next, I would go into the CSS files and I would update the styling to be a bit more unique. It's kinda bland, and I just want to make it look better. After that, I would want to update the widget so that when you open it, it has all the products listed and that it would filter down visually to three products instead of just having a blank screen and then having three products be returned after search. I think that would be a better UI experience.
