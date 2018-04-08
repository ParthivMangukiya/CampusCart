<?php

class DbOperation {

    private $con;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
    }

    public function createUser($name,$email, $mobile_no, $photo_url, $date_of_registration, $uid) {
        $res = [];
        if (!($this->isUserExists($email))) {
            $stmt = $this->con->prepare("INSERT INTO user(name, email, mobile_no, photo_url, date_of_registration, firebase_uid) values(? ,?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssss",$name, $email, $mobile_no, $photo_url, $date_of_registration, $uid);
            $result = $stmt->execute();
            $res['id'] = $stmt->insert_id;
            $stmt->close();
            if ($result) {
                $res['error'] = 0;
            } else {
                $res['error'] = 1;
            }
        } else {
            $res['error'] = 2;
        }


        return $res;
    }

    public function getUserByFirebaseUserId($id) {
        $stmt = $this->con->prepare("SELECT * FROM user WHERE firebase_uid = ?");
        if($stmt){
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $student = $stmt->get_result();
            $stmt->close();
            return $student;
        }else{
            return false;
        }
    }

    public function getUserByUserId($id) {
        $stmt = $this->con->prepare("SELECT * FROM user WHERE id = ?");
        if($stmt){
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $student = $stmt->get_result();
            $stmt->close();
            return $student;
        }else{
            return false;
        }
    }

    public function getUserIdByEmailId($email) {
        $stmt = $this->con->prepare("SELECT id FROM user WHERE email = ?");
        if($stmt){
            $stmt->bind_param("s", $id);
            $stmt->execute();
            $student = $stmt->get_result();
            $stmt->close();
            return $student;
        }else{
            return false;
        }
    }

    public function getOrderById($id) {
        $stmt = $this->con->prepare("SELECT * FROM orders WHERE id = ?");
        if($stmt){
            $stmt->bind_param("i", $id);
            $stmt->execute();
            $student = $stmt->get_result();
            $stmt->close();
            return $student;
        }else{
            return false;
        }
    }

    public function getItemIdByOrderId($id) {
        $stmt = $this->con->prepare("SELECT iid FROM order_items WHERE id = ?");
        if($stmt){
            $stmt->bind_param("i", $id);
            $stmt->execute();
            $student = $stmt->get_result();
            $stmt->close();
            return $student;
        }else{
            return false;
        }
    }

    public function getAllUsers() {
        $stmt = $this->con->prepare("SELECT * FROM user");
        $stmt->execute();
        $students = $stmt->get_result();
        $stmt->close();
        return $students;
    }

    public function getAllItems() {
        $stmt = $this->con->prepare("SELECT * FROM item");
        $stmt->execute();
        $students = $stmt->get_result();
        $stmt->close();
        return $students;
    }

    public function getAllShops() {
        $stmt = $this->con->prepare("SELECT * FROM shopkeeper");
        $stmt->execute();
        $students = $stmt->get_result();
        $stmt->close();
        return $students;
    }


    public function createItem($price, $name, $category, $description, $url) {
        $res = [];
        if (!($this->isItemExists($name))) {
            $stmt = $this->con->prepare("INSERT INTO item(price, name, category, description,imageUri) values(?, ?, ?, ?, ?)");
            $stmt->bind_param("sssss",$price, $name, $category, $description, $url);
            $result = $stmt->execute();
            $res['id'] = $stmt->insert_id;
            $stmt->close();
            if ($result) {
                $res['error'] = 0;
            } else {
                $res['error'] = 1;
            }
        } else {
            $res['error'] = 2;
        }


        return $res;
    }

    public function createOrder($uid, $otp, $amount, $color) {
        $res = [];
       
            $stmt = $this->con->prepare("INSERT INTO orders(uid, otp, amount, color) values(?, ?, ?, ?)");
            $stmt->bind_param("iidi",$uid, $otp, $amount,$color);
            $result = $stmt->execute();
            $res['id'] = $stmt->insert_id;
            $stmt->close();
            if ($result) {
                $res['error'] = 0;
            } else {
                $res['error'] = 1;
            }
        

        return $res;
    }

    public function createOrderItems($id, $iid) {
        $res = [];
       		
            $stmt = $this->con->prepare("INSERT INTO order_items(id, iid) values(?, ?)");
            $stmt->bind_param("ii",$id, $iid);
            $result = $stmt->execute();
            //$res['id'] = $stmt->insert_id;
            $stmt->close();
            if ($result) {
                $res['error'] = 0;
            } else {
                $res['error'] = 1;
            }
        

        return $res;
    }

    private function isItemExists($firebase_uid) {
        $stmt = $this->con->prepare("SELECT * from item WHERE name = ?");
        if ($stmt) {
            $stmt->bind_param("s", $firebase_uid);
            $stmt->execute();
            $stmt->store_result();
            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            return true;
        }
    }

    private function isUserExists($firebase_uid) {
        $stmt = $this->con->prepare("SELECT email from user WHERE email = ?");
        if ($stmt) {
            $stmt->bind_param("s", $firebase_uid);
            $stmt->execute();
            $stmt->store_result();

            $num_rows = $stmt->num_rows;
            $stmt->close();
            return $num_rows > 0;
        } else {
            return true;
        }
    }

    public function getItemById($id) {
        $stmt = $this->con->prepare("SELECT * FROM item WHERE id=?");
        if($stmt){
            $stmt->bind_param("i", $id);
            $stmt->execute();
            $student = $stmt->get_result();
            $stmt->close();
            return $student;
        }else{
            return false;
        }
    }



}
    