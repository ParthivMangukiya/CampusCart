<?php

class DbOperation {

    private $con;

    function __construct() {
        require_once dirname(__FILE__) . '/DbConnect.php';
        $db = new DbConnect();
        $this->con = $db->connect();
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


    public function createItem($price, $name, $category, $description) {
        $res = [];
        if (!($this->isItemExists($name))) {
            $stmt = $this->con->prepare("INSERT INTO item(price, name, category, description) values(?, ?, ?, ?)");
            $stmt->bind_param("ssss",$price, $name, $category, $description);
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

    public function getItemById($user_id) {
        $stmt = $this->con->prepare("SELECT * FROM item WHERE id=?");
        if($stmt){
            $stmt->bind_param("i", $user_id);
            $stmt->execute();
            $student = $stmt->get_result();
            $stmt->close();
            return $student;
        }else{
            return false;
        }
    }



}
    