# PGdP Seam Carving Visualizer
## What is this?
Week 3's PGdP Homework #3 - Seam Carving has strained all of our nerves. So to better debug and visualize what's 
actually going on in our code, we wrote this visualizer. It is - by far - not optimal. But. It works, displays your 
shrinking picture, the mask, gradient magnitude and mask combined with gradient.

## How do I get it to work?
1) Copy your SeamCarving.java in src/main/java/io/qemfd/pgdp/seam/visualizer
2) Run the project via gradle (`gradlew run`) (the "run" button in IntelliJ)
3) Done! It might lag a bit, as the program buffers all step images of the process (~5GB!),
but after a short while it runs smoothly.
4) Now you can go through all the generated pictures with the 'j' and the 'k' keys
