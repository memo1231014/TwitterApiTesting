# TwitterApiTesting
Simple Example for twitter Api


Simple application to show your twitter followers informations (name,handle,bio,images and tweets)

you will find different ways to work with twitter api i used more than one depend on what’s the easiest for everything and to show that we can work with more than one ways 

for example i used fabric to login (Just add twitter button and get user details on button response ,
i used also twitter4j for getting user followers and tweets it really have amazing methods that help to do it.

while paging through follower i decided to use my custom paging to control it 100% rather than libraries i wrote it alone and recalled it to make code cleaner i used built in swipeLayout to refresh 

I cached the followers to let user check it if he is offline i did using sqlite data base i only added 20 followers within it to save mobile memory


I used ImageLoader library to load and cache images i chooses it because 
i used to work with it so it will save my time it’s the best for our case we no need more powerful than ImageLoader

I used also Butter Knife to save time and to make the code more clean

I wrote my code depend on this guide lines to make it easy to reuse and understand
https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md

About adding multiAccounts my Idea is add button that logout from the current mail and reopen the main screen while i have user keys and usedId so i can save it anywhere say within sqlite and let user go to re login with different account so i have many account within the database user now can choose any of them


i many cases because i only created the main jobs for examples
what about server error , internet connections error , what about there's no tweets , no follwers , what about banned accounts
also on i am aware with all test cases but not coverd because this version just for testing 

Thanks so much have the best days :)




                                                
                                              
