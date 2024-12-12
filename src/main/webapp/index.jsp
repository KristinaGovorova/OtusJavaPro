<!DOCTYPE html>
<head>
    <title>Calculator</title>
</head>
<body>
    <h1>Calculator</h1>
    <h2>Add</h2>
    <form action="/calculator/add" method="get">
        <label for="a">Enter A:</label>
        <input type="text" id="a" name="a" required>
        <br>
        <label for="b">Enter B:</label>
        <input type="text" id="b" name="b" required>
        <br>
        <input type="submit" value="Submit">
    </form>
    <h2>Subtract</h2>
    <form action="/calculator/subtract" method="get">
        <label for="a">Enter A:</label>
        <input type="text" id="a" name="a" required>
        <br>
        <label for="b">Enter B:</label>
        <input type="text" id="b" name="b" required>
        <br>
        <input type="submit" value="Submit">
    </form>
    <h2>Divide</h2>
    <form action="/calculator/div" method="get">
        <label for="a">Enter A:</label>
        <input type="text" id="a" name="a" required>
        <br>
        <label for="b">Enter B:</label>
        <input type="text" id="b" name="b" required>
        <br>
        <input type="submit" value="Submit">
    </form>
    <h2>Multiply</h2>
    <form action="/calculator/multiply" method="get">
        <label for="a">Enter A:</label>
        <input type="text" id="a" name="a" required>
        <br>
        <label for="b">Enter B:</label>
        <input type="text" id="b" name="b" required>
        <br>
        <input type="submit" value="Submit">
    </form>
</body>
</html>
