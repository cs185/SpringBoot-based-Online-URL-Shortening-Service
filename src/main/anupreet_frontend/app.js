const express = require('express');
const app = express();
const PORT = 3000;

app.set('view engine', 'ejs');
app.use(express.static('public'));


app.get('/', (req, res) => {
    res.redirect('/login');
});


app.get('/login', (req, res) => {
    res.render('login');
});


app.get('/signup', (req, res) => {
    res.render('signup');
});

app.get('/about', (req, res) => {
    const team = [
        { name: 'Mihir Prakash', image: '/images/mihir.jpeg' },
        { name: 'Carl Shen',  image: '/images/carl.jpeg' },
        { name: 'Anupreet Sihra',  image: '/images/anu.jpeg' },
        { name: 'Jiahao Zhou', image: '/images/jay.jpeg' },
        { name: 'Kaushal Kumar Agarwal',  image: '/images/kaushal.jpeg' },
        { name: 'Kuang-Tse Hung', image: '/images/kuang.jpeg' },
        { name: 'Jeffrey J. Sam',  image: '/images/jeffreysam.jpeg' }
    ];
    res.render('about', { team: team });
});

app.get('/dashboard', (req, res) => {
    const urls = [
        { originalUrl: 'https://example.com', shortUrl: 'https://short.url/ex' },
        { originalUrl: 'https://anotherexample.com', shortUrl: 'https://short.url/ae' }
    ];
    res.render('dashboard', { urls: urls });
});

app.get('/create', (req, res) => {

    res.render('create');
});

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
