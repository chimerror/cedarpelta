# Cedarpelta, A simple ebooks-style Twitter bot

This is a simple ebooks-style Twitter bot. Kind of barebones right now, but I'm working on it.

## Usage Steps

I assume you can figure out how to run a Clojure script. More detailed directions coming soon!

1. Create a new account for your bot. You'll have to have a mobile number for it to be able to use the API.
2. Follow your source account.
3. Go to the [https://apps.twitter.com/](Twitter Apps Page) to create an app for your bot. Make sure to generate an access token to be able to post.
4. Update the keys and source timeline in resources/config.edn
5. Run it! It'll grab tweets from the source timeline, make a markov chain of them, make a random walk of it, and then tweet it.

## License

Copyright © 2016 Jason Mitchell
Distributed under the MIT License
