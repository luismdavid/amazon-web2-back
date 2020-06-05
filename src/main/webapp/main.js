class User {
  email;
  username;
  password;
  firstName;
  lastName;
  identification;
  phone;
}

const objectToFormdata = obj => {
  const form = new FormData();
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      const value = obj[key];
      form.append(key, value);
    }
  }
  return form;
};

const registerUser = user => {
  const options = {
    method: 'POST',
    body: objectToFormdata(user),
  };
  return fetch('/Register', options).then(res => res.json());
};

const loginUser = user => {
  const options = {
    method: 'POST',
    body: objectToFormdata(user),
  };
  return fetch('/Login', options).then(res => res.json());
};

const deleteUser = () => {
  const options = {
    method: 'DELETE',
  };
  return fetch('/DeleteUser', options).then(res => res.json());
};
