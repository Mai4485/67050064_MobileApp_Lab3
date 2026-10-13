from sqlalchemy import Column, Integer, String
from database import Base

class Student(Base):
    __tablename__ = "students"

    id = Column(Integer, primary_key=True, index=True)
    student_code = Column(String, nullable=False)
    name = Column(String, nullable=False)
    major = Column(String, nullable=False)