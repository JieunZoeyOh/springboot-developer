const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
  deleteButton.addEventListener('click', event => {
    const id = document.getElementById('article-id').value;
    fetch(`/api/articles/${id}`, {method: 'DELETE'})
      .then(() => {
        alert('삭젝 완료되었습니다.');
        location.replace('/articles');
      });
  });
}

const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
  modifyButton.addEventListener('click', () => {
    const params = new URLSearchParams(location.search);
    const id = params.get('id');

    fetch(`/api/articles/${id}`, {
      method: 'PUT',
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify({
        title: document.getElementById('title').value,
        content: document.getElementById('content').value
      })
    }).then(() => {
      alert('수정이 완료되었습니다.');
      location.replace(`/articles/${id}`);
    });
  });
}

const createButton = document.getElementById('create-btn');
if (createButton) {
  createButton.addEventListener('click', () => {
    fetch('/api/articles', {
      method: 'POST',
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify({
        title: document.getElementById('title').value,
        content: document.getElementById('content').value
      })
    }).then(() => {
      alert('등록 완료되었습니다.');
      location.replace('/articles');
    });
  });
}