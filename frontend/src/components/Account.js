import React, {Component} from "react";
import {connect} from "react-redux";
import {userAccount} from '../actions/AccountActions';
import DataService from '../service/DataService';
import { Link } from "react-router-dom";


class Account extends Component {

    password = 'sddsvwe34s';

    constructor(props) {
        super(props);
        this.handelSubmit = this.handelSubmit.bind(this);
        this.handleCancel = this.handelSubmit.bind(this);
    }

    async componentDidMount() {
        const response = await DataService.userProfileRequest();
        const account = await response.json();
        this.props.userAccount(account);
    }

    async handelSubmit(event) {
        event.preventDefault();
        const editForm = document.forms["edit-form"];
        const firstName = editForm.elements["firstName"].value;
        const lastName = editForm.elements['lastName'].value;
        const patronymic = editForm.elements['patronymic'].value;
        const email = editForm.elements['email'].value;
        const address = editForm.elements['address'].value;
        const phone = editForm.elements['phone'].value;
        const oldPassword = editForm.elements['oldPassword'].value;
        const newPassword = editForm.elements['newPassword'].value;
        const newAccountData = {
            firstName,
            lastName,
            patronymic,
            email,
            address,
            phone,
            oldPassword,
            newPassword
        };
        const response = await DataService.editAccountDataRequest(newAccountData);
        if (!response.ok)
            return false;
        const account = await response.json();
        this.props.userAccount(account);
        this.props.history.push('/catalog');
    }

    handleCancel() {
        this.props.history.push('/catalog');
    }

    render() {
        const account = this.props.account || {};
        return (
            <div className="w-25 account-edit-container">
                <h1 className="m-4">Account</h1>
                <form name="edit-form" onSubmit={ this.handelSubmit }>
                    <div className="form-group">
                        <label><b>Fist name</b></label>
                        <input type="text" className="form-control form-control-lg " id="fistName"
                               name="firstName" placeholder="Your fist name"
                               defaultValue={ account.firstName || '' } required autoFocus/>
                    </div>
                    <div className="form-group">
                        <label><b>Last name</b></label>
                        <input type="text" className="form-control form-control-lg" id="lastName"
                               name="lastName" placeholder="Your last name"
                               defaultValue={ account.lastName || '' } required/>
                    </div>
                    <div className="form-group">
                        <label><b>Patronymic</b></label>
                        <input type="text" className="form-control form-control-lg" id="patronymic"
                               name="patronymic" placeholder="Your patronymic"
                               defaultValue={ account.patronymic || '' }/>
                    </div>
                    <div className="form-group">
                        <label><b>Email</b></label>
                        <input type="email" className="form-control form-control-lg" id="email"
                               name="email" placeholder="Your email" required
                               defaultValue={ account.email  || '' }/>
                    </div>
                    <div className="form-group">
                        <label><b>Address</b></label>
                        <input type="text" className="form-control form-control-lg" id="address"
                               name="address" placeholder="Your address" required
                               defaultValue={ account.address || '' }/>
                    </div>
                    <div className="form-group">
                        <label><b>Phone</b></label>
                        <input type="text" className="form-control form-control-lg" id="phone"
                               name="phone" placeholder="Your phone" required
                               defaultValue={ account.phone || '' }/>
                    </div>
                    <div className="form-group">
                        <label><b>Old password</b></label>
                        <input type="password" className="form-control form-control-lg" id="oldPassword"
                               name="oldPassword" placeholder="Old password" required
                               defaultValue={this.password}/>
                    </div>
                    <div className="form-group">
                        <label><b>New password</b></label>
                        <input type="password" className="form-control form-control-lg" id="newPassword"
                               name="newPassword" placeholder="New password" required minLength="8"
                               defaultValue={this.password}/>
                    </div>
                    <div className="form-group mt-4 row">
                        <div className="col-6">
                            <button type="submit"
                                    className="btn btn-lg btn-primary btn-block">Save</button>
                        </div>
                        <div className="col-6">
                            <Link className="w-100 btn btn-lg btn-primary btn-warning" to="/catalog" role="button">Cancel</Link>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        account: state.userState.user
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        userAccount: (account) => dispatch(userAccount(account))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Account);