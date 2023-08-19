const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
  deleteButton.addEventListener('click', () => {
    const id = document.getElementById('article-id').value;

    function success() {
      alert('삭제가 완료되었습니다.');
      location.replace('/articles');
    }

    function fail() {
      alert('삭제 실패했습니다.');
      location.replace('/articles');
    }

    httpRequest('DELETE', `/api/articles/${id}`, null, success, fail);

  });
}

const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
  modifyButton.addEventListener('click', () => {
    const params = new URLSearchParams(location.search);
    const id = params.get('id');

    const body = JSON.stringify({
      title: document.getElementById('title').value,
      content: document.getElementById('content').value
    });

    function success() {
      alert('수정 완료되었습니다.');
      location.replace(`/articles/${id}`);
    }

    function fail() {
      alert('수정 실패했습니다.');
      location.replace(`/articles/${id}`);
    }

    httpRequest("PUT", `/api/articles/${id}`, body, success, fail);
  });
}

const createButton = document.getElementById('create-btn');

if (createButton) {
  // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다
  createButton.addEventListener('click', () => {
    const body = JSON.stringify({
      title: document.getElementById('title').value,
      content: document.getElementById('content').value
    });

    function success() {
      alert('등록 완료되었습니다.');
      location.replace('/articles');
    }

    function fail() {
      alert('등록 실패했습니다.');
      location.replace('/articles');
    }

    httpRequest('POST','/api/articles', body, success, fail);
  });
}


// 쿠키를 가져오는 함수
function getCookie(key) {
  let result = null;
  const cookie = document.cookie.split(';');
  cookie.some(function (item) {
    item = item.replace(' ', '');

    const dic = item.split('=');

    if (key === dic[0]) {
      result = dic[1];
      return true;
    }
  });

  return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
  fetch(url, {
    method: method,
    headers: { // 로컬 스토리지에서 액세스 토큰 값을 가져와 헤더에 추가
      Authorization: 'Bearer ' + localStorage.getItem('access_token'),
      'Content-Type': 'application/json',
    },
    body: body
  }).then(response => {
    if (response.status === 200 || response.status === 201) {
      return success();
    }

    const refresh_token = getCookie('refresh_token');

    if (response.status === 401 && refresh_token) {
      fetch('/api/token', {
        method: 'POST',
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('access_token'),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          refreshToken: getCookie('refresh_token'),
        }),
      })
        .then(res => {
          if (res.ok) {
            return res.json();
          }
        })
        .then(result => { // 재발급이 성공하면 로컬 스토리지값을 새로운 액세스 토큰으로 교체
          localStorage.setItem('access_token', result.accessToken);
          httpRequest(method, url, body, success, fail);
        })
        .catch(error => fail());
    } else {
      return fail();
    }

  });
}