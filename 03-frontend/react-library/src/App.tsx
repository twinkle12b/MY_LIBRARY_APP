
import { Redirect, Route, Switch, useHistory } from 'react-router-dom';
import './App.css';
import { HomePage } from './layouts/HomePage/HomePage';
import { Footer } from './layouts/NavBarAndFooter/Footer';
import { Navbar } from './layouts/NavBarAndFooter/NavBar';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { BookCheckoutPage } from './layouts/BookCheckoutPage/BookCheckoutPage';
import { oktaConfig } from './lib/OktaConfig';
import { OktaAuth, toRelativeUrl } from '@okta/okta-auth-js';
import { LoginCallback, SecureRoute, Security } from '@okta/okta-react';
import LoginWidget from './Auth/LoginWidget';
import { ReviewListPage } from './layouts/BookCheckoutPage/ReviewListPage/ReviewListPage';
import { ShelfPage } from './layouts/ShelfPage/ShelfPage';
import { MessagesPage } from './layouts/MessagesPage/MessagesPage';
import { ManageLibraryPage } from './layouts/ManageLibraryPage/ManageLibraryPage';


const oktaAuth = new OktaAuth(oktaConfig);

function App() {

  const history = useHistory();

  const customAuthHandler = () => {
    history.push('/login');
  };

  const restoreOriginalUri = async (_oktaAuth: any, originalUri: any) => {
    history.replace(toRelativeUrl(originalUri || '', window.location.origin));
  };

  return (
    <div className='d-flex flex-column min-vh-100'>
      <Security oktaAuth={oktaAuth} restoreOriginalUri={restoreOriginalUri} onAuthRequired={customAuthHandler}>
        <Navbar />
        <div className='flex-grow-1'>
          <Switch>
            <Route path='/' exact>
              <Redirect to='/homePage' />
            </Route>
            <Route path='/homePage'>
              <HomePage />
            </Route>
            <Route path='/search'>
              <SearchBooksPage />
            </Route>
            <Route path='/allReviews/:bookId'>
              <ReviewListPage />
            </Route>
            <Route path='/checkout/:bookId'>
              <BookCheckoutPage />
            </Route>
            <Route path="/login" render={() => <LoginWidget config={oktaConfig} />} />
            <Route path="/login/callback" component={LoginCallback} />
            <SecureRoute path="/shelf"><ShelfPage /></SecureRoute>
            <SecureRoute path="/messages"><MessagesPage /></SecureRoute>
            <SecureRoute path="/admin"><ManageLibraryPage /></SecureRoute>
          </Switch>
        </div>
        <Footer />
      </Security>
    </div>

  );
}

export default App;
